package org.v8LogScanner.cmdScanner;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;

import org.v8LogScanner.cmdAppl.CmdCommand;
import org.v8LogScanner.rgx.RegExp;
import org.v8LogScanner.rgx.RegExp.PropTypes;
import org.v8LogScanner.rgx.ScanProfile;
import org.v8LogScanner.rgx.ScanProfile.GroupTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CmdAddGroupBy implements CmdCommand {

    public String getTip() {

        V8LogScannerAppl appl = V8LogScannerAppl.instance();
        if (appl.profile.getGroupType() == GroupTypes.BY_FILE_NAMES)
            return "[" + GroupTypes.BY_FILE_NAMES.toString() + "]";
        else if (getSizeGroupingProps(appl.profile.getRgxList()) == 0)
            return "[default props]";
        else {
            List<String> groups = appl.profile.getRgxList()
                    .stream()
                    .flatMap(event -> {
                        // BLOCKTEST EVAL: https://github.com/ripreal/V8LogScanner/blob/3789156a0ab07c4b2cdd91e8165ca733ecc01905/src/main/java/org/v8LogScanner/cmdScanner/CmdAddGroupBy.java#L25-L29
                       /*
                        @blocktest().given(event, new RegExp()).setup(() -> {
                            event.getGroupingProps().add(PropTypes.Event);
                            event.getGroupingProps().add(PropTypes.Context);
                            event.getGroupingProps().add(PropTypes.Time);
                        }).checkEq(lambdaReturn.sorted().collect(Collectors.toList()).toString(), "[ANY:Context, ANY:Event, ANY:Time]");
                        */
                        List<String> descr = new ArrayList<>();
                        event.getGroupingProps().forEach((prop) -> descr.add(event + ":" + prop));
                        return descr.stream();
                    })
                    .collect(Collectors.toList());
            return groups.toString();
        }
    }

    public void execute() {

        V8LogScannerAppl appl = V8LogScannerAppl.instance();

        appl.getConsole().println("");

        String userInput = null;
        // asking about group type (by file name or property)
        if (appl.profile.getRgxOp() == ScanProfile.RgxOpTypes.HEAP_OP){
            GroupTypes[] groupTypes = GroupTypes.values();
            userInput = appl.getConsole().askInputFromList("Choose group type", groupTypes);

            if (userInput == null)
                return;

            GroupTypes userGroupType = (groupTypes[Integer.parseInt(userInput)]);
            appl.profile.setGroupType(userGroupType);
            if (userGroupType == GroupTypes.BY_FILE_NAMES)
                return;
        }

        // asking about event for filter
        userInput = appl.getConsole().askInputFromList("Choose event to group", appl.profile.getRgxList());

        if (userInput == null)
            return;

        // Asking about prop for filter
        RegExp rgx = appl.profile.getRgxList().get(Integer.parseInt(userInput));
        ArrayList<PropTypes> props = rgx.getPropsForGrouping();
        userInput = appl.getConsole().askInputFromList("Choose property to group", props);

        if (userInput == null)
            return;

        PropTypes prop = props.get(Integer.parseInt(userInput));
        rgx.getGroupingProps().add(prop);

    }

    private long getSizeGroupingProps(List<RegExp> rgxList) {
        long userGroupProps = rgxList.stream().
                flatMap(n -> n.getGroupingProps().stream()).
                count();
        return userGroupProps;
    }

}
