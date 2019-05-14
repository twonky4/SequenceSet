pipeline {
    options {
        buildDiscarder(logRotator(numToKeepStr: '5', artifactNumToKeepStr: '5'))
    }
    agent any
    tools {
        maven 'maven3'
        jdk 'jdk8'
    }
    stages {
        stage ('Build') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'maven_deployment', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                  sh 'echo "<settings><servers><server><id>nexus</id><username>$USERNAME</username><password>$PASSWORD</password></server><server><id>releases</id><username>$USERNAME</username><password>$PASSWORD</password></server><server><id>snapshots</id><username>$USERNAME</username><password>$PASSWORD</password></server></servers><mirrors><mirror><id>nexus</id><mirrorOf>*</mirrorOf><url>http://kose.one/nexus/repository/maven-public/</url></mirror></mirrors><profiles><profile><id>nexus</id><repositories><repository><id>central</id><url>http://central</url><releases><enabled>true</enabled></releases><snapshots><enabled>true</enabled></snapshots></repository></repositories><pluginRepositories><pluginRepository><id>central</id><url>http://central</url><releases><enabled>true</enabled></releases><snapshots><enabled>true</enabled></snapshots></pluginRepository></pluginRepositories></profile></profiles><activeProfiles><activeProfile>nexus</activeProfile></activeProfiles></settings>" > settings.xml'
                  sh 'mvn -s settings.xml clean deploy'
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
        }
    }
}
