#!/bin/bash
project_name=$1
sha=$2
coverage=$3
project_name=$(echo ${project_name} | tr _ -)

if [[ ${project_name} == george-haddad-cardme ]]; then
  echo "Patching george-haddad-cardme"
  sed -i.bak "s/1.5/1.8/" pom.xml
  rm pom.xml.bak
fi

if [[ ${project_name} == mvel-mvel && ${sha} == "bac95e0cf8884a0713693161c589514a8a43fe87" ]]; then
  echo "Patching mvel-mvel"
  if [[ -z $(grep "@org" ./src/test/java/org/mvel2/tests/core/ControlFlowTests.java) ]]; then
    if [[ "$OSTYPE" == "darwin"* ]]; then
      sed -i '' $'/public void testCalculateAge/i\\\n    @org.junit.Ignore' ./src/test/java/org/mvel2/tests/core/ControlFlowTests.java
    else
      sed -i $'/public void testCalculateAge/i\\\n    @org.junit.Ignore' ./src/test/java/org/mvel2/tests/core/ControlFlowTests.java
    fi
  fi
fi

if [[ ${project_name} == Harium-keel && ${sha} == "e4e6a302eb6b7dcf6dab5d1167cf6ceef836d4db" ]]; then
  echo "Patching Harium-keel"
  sed -i.bak "s/<version>[/<version>/g" pom.xml
  rm pom.xml.bak
  
  sed -i.bak "s/,)<\/version>/<\/version>/g" pom.xml
  rm pom.xml.bak
fi

if [[ ${project_name} == jai-imageio-jai-imageio-core && ${sha} == "f81bc1ab19faa210ad289c6ae2588bc1157fd07a" ]]; then
  echo "Patching jai-imageio-jai-imageio-core"
  sed -i.bak 's/<source>1.6<\/source>/<source>7<\/source>/' pom.xml
  rm pom.xml.bak
  
  sed -i.bak 's/<target>1.6<\/target>/<target>7<\/target>/' pom.xml
  rm pom.xml.bak
fi

if [[ ${project_name} == pfmiles-min-velocity && ${sha} == "4e8ec4f64681c1d6941bf8d90a51849e87b86cf5" ]]; then
  echo "Patching pfmiles-min-velocity"
  sed -i.bak 's/3.8.1/4.13.2/g' pom.xml
  rm pom.xml.bak
fi

if [[ ${project_name} == SoftEngResearch-imop ]]; then
  echo "Patching SoftEngResearch-imop"
  mkdir -p maven-plugin/src/test
  if [[ ${coverage} == "true" ]]; then
    rm -rf maven-plugin/src/main/resources/
  fi
fi

if [[ ${project_name} == tommyettinger-RegExodus && ${coverage} == "true" ]]; then
  echo "Patching tommyettinger-RegExodus"
  sed -i.bak 's|<argLine>-Dfile.encoding=${project.build.sourceEncoding}</argLine>|<argLine>@{argLine} -Dfile.encoding=${project.build.sourceEncoding}</argLine>|' pom.xml
  rm pom.xml.bak
fi

if [[ ${project_name} == STEMLab-JIneditor && ${coverage} == "true" ]]; then
  echo "Patching STEMLab-JIneditor"
  sed -i.bak 's/<argLine>-Xmx1024m<\/argLine>/<argLine>@{argLine} -Xmx1024m<\/argLine>/' pom.xml
  rm pom.xml.bak
fi

if [[ ${project_name} == jruby-jcodings && ${coverage} == "true" ]]; then
  echo "Patching jruby-jcodings"
  sed -i.bak 's/<argLine>-Dfile.encoding=UTF-8<\/argLine>/<argLine>@{argLine} -Dfile.encoding=UTF-8<\/argLine>/' pom.xml
  rm pom.xml.bak
fi

if [[ ${project_name} == ATLANTBH-owl ]]; then
  echo "Patching ATLANTBH-owl"
  sed -i.bak 's/<argLine>-Dfile.encoding=UTF-8<\/argLine>/<argLine>@{argLine} -Dfile.encoding=UTF-8<\/argLine>/' pom.xml
  rm pom.xml.bak
  
  sed -i.bak '
/<plugin>/{
  :a
  N
  /<\/plugin>/!ba
  /<groupId>com.github.eirslett<\/groupId>/d
}
' pom.xml
  rm pom.xml.bak
fi


if [[ ${project_name} == dingjs-javaagent ]]; then
  echo "Patching dingjs-javaagent"
  sed -i.bak 's|<project\.build\.target>1\.6</project\.build\.target>|<project.build.target>1.8</project.build.target>|g' pom.xml
  rm pom.xml.bak
fi

if [[ ${project_name} == magdel-MapNav ]]; then
  echo "Patching magdel-MapNav"
  sed -i.bak '
/<plugin>/,/<\/plugin>/{
  /<artifactId>maven-compiler-plugin<\/artifactId>/,/<\/plugin>/{
    s|<source>.*</source>|<source>1.8</source>|
    s|<target>.*</target>|<target>1.8</target>|
    /<compilerArgs>/,/<\/compilerArgs>/d
  }
}
' pom.xml
  rm pom.xml.bak
fi

if [[ ${project_name} == tud-fop-j-Algo ]]; then
  echo "Patching tud-fop-j-Algo"
  
  sed -i.bak '
/<plugin>/{
  :a
  N
  /<\/plugin>/!ba
  /<artifactId>maven-antrun-plugin<\/artifactId>/d
}
' pom.xml
  rm pom.xml.bak
fi

if [[ ${project_name} == demidenko05-beigesoft-accounting ]]; then
  echo "Patching demidenko05-beigesoft-accounting"
  sed -i.bak 's|<java\.version>1\.7</java\.version>|<java.version>1.8</java.version>|g' pom.xml
  rm pom.xml.bak
fi
