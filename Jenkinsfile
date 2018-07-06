@Library('jenkins-groovy-lib')
import startNlWebIndus
startNlWebIndus(startMongo: false, disableSonar: false, sonarCloud: true)

node('master') {
    stage('archive'){
        archiveArtifacts '**/*.zip'
    }
}
