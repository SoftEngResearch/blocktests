#!/bin/bash
TIMEOUT="10800s"
CHECK_PROJECT_TIMEOUT="3600s"
SKIP="-Dmaven.nbm.verify=skip -Dcheckstyle.skip -Drat.skip -Denforcer.skip -Danimal.sniffer.skip -Dmaven.javadoc.skip -Dfindbugs.skip -Dwarbucks.skip -Dmodernizer.skip -Dimpsort.skip -Dpmd.skip -Dxjc.skip -Djacoco.skip -Dinvoker.skip -DskipDocs -DskipITs -Dmaven.plugin.skip -Dlombok.delombok.skip -Dlicense.skipUpdateLicense -Dremoteresources.skip -Dlicense.skip -Dgpg.skip -Dspotbugs.skip -Dmaven.antrun.skip"
SKIP_WITH_JACOCO="-Dmaven.nbm.verify=skip -Dcheckstyle.skip -Drat.skip -Denforcer.skip -Danimal.sniffer.skip -Dmaven.javadoc.skip -Dfindbugs.skip -Dwarbucks.skip -Dmodernizer.skip -Dimpsort.skip -Dpmd.skip -Dxjc.skip -Dinvoker.skip -DskipDocs -DskipITs -Dmaven.plugin.skip -Dlombok.delombok.skip -Dlicense.skipUpdateLicense -Dremoteresources.skip -Dlicense.skip -Dgpg.skip -Dspotbugs.skip -Dmaven.antrun.skip"
TMP_DIR=/tmp
