package cn.lzgabel.converter.processing.event;

import javax.xml.parsers.DocumentBuilderFactory;
import io.camunda.zeebe.model.bpmn.builder.zeebe.MessageBuilder;
import io.camunda.zeebe.model.bpmn.impl.instance.MessageImpl;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import cn.lzgabel.converter.bean.event.intermediate.IntermediateCatchEventDefinition;
import cn.lzgabel.converter.bean.event.intermediate.MessageIntermediateCatchEventDefinition;
import cn.lzgabel.converter.bean.event.intermediate.TimerIntermediateCatchEventDefinition;
import cn.lzgabel.converter.bean.event.start.EventType;
import cn.lzgabel.converter.processing.BpmnElementProcessor;
import io.camunda.zeebe.model.bpmn.builder.AbstractFlowNodeBuilder;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.model.xml.impl.instance.ModelTypeInstanceContext;
import org.junit.Test;
import static org.junit.Assert.*;
import static cn.lzgabel.converter.processing.event.IntermediateCatchEventProcessor.*;

public class IntermediateCatchEventProcessorBlockTest {

    @Test(expected = NullPointerException.class)
    public void test1() throws Exception {
        MessageBuilder messageBuilder = new MessageBuilder(new io.camunda.zeebe.model.bpmn.impl.BpmnModelInstanceImpl(new org.camunda.bpm.model.xml.impl.ModelImpl("foo"), new org.camunda.bpm.model.xml.impl.ModelBuilderImpl("foo"), new org.camunda.bpm.model.xml.impl.instance.DomDocumentImpl(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument())), new io.camunda.zeebe.model.bpmn.impl.instance.MessageImpl(new ModelTypeInstanceContext(null, null, null)));
        String messageName = "name";
        String messageCorrelationKey = "=foo";
        if (StringUtils.isNotBlank(messageName)) {
        }
        if (StringUtils.isNotBlank(messageCorrelationKey)) {
            // '='
            if (StringUtils.startsWith(messageCorrelationKey, ZEEBE_EXPRESSION_PREFIX)) {
                messageBuilder.zeebeCorrelationKey(messageCorrelationKey);
            } else {
            }
        }
    }

    @Test(expected = NullPointerException.class)
    public void test2() throws Exception {
        MessageBuilder messageBuilder = new MessageBuilder(new io.camunda.zeebe.model.bpmn.impl.BpmnModelInstanceImpl(new org.camunda.bpm.model.xml.impl.ModelImpl("foo"), new org.camunda.bpm.model.xml.impl.ModelBuilderImpl("foo"), new org.camunda.bpm.model.xml.impl.instance.DomDocumentImpl(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument())), new io.camunda.zeebe.model.bpmn.impl.instance.MessageImpl(new ModelTypeInstanceContext(null, null, null)));
        String messageName = "name";
        String messageCorrelationKey = "foo";
        if (StringUtils.isNotBlank(messageName)) {
        }
        if (StringUtils.isNotBlank(messageCorrelationKey)) {
            // '='
            if (StringUtils.startsWith(messageCorrelationKey, ZEEBE_EXPRESSION_PREFIX)) {
                messageBuilder.zeebeCorrelationKey(messageCorrelationKey);
            } else {
            }
        }
    }
}
