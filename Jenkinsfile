#!groovy
@NonCPS
def getVersionsLib() {
    def metadata = new XmlSlurper().parse('http://18.159.141.245:8081/nexus/content/repositories/releases/hw/libs/common/helloworldlib/maven-metadata.xml')
    def versionsLib = new ArrayList()
    for (version in metadata.versioning.versions) {
        versionsLib.add(version.text())
    }
    def versions = metadata.depthFirst().findAll { it.name() == 'version' }
    // versions.addAll(metadata.versioning.versions.version*.text())
    // println versions[3]
    // return versions.join('\n')
    // return versionsLib[0]
    return versions
pipeline {
    agent {
        label('agent')
    }
    options {
        timestamps()
    }
    parameters {
        booleanParam defaultValue: false, description: 'Building All Apps', name: 'BuildAllApp'
        choice(name: 'VERSION_LIB', choices: [getVersionsLib()], description: 'Choise Library Versions')
    }
    stages {
        stage('Gather Deployment Parameters') {
            steps {
                // timeout(time: 30, unit: 'SECONDS') {
                //     /* groovylint-disable-next-line NestedBlockDepth */
                //     // script {
                //     //     // Show the select input modal
                //     //    /* groovylint-disable-next-line NoDef */
                //     //    def INPUT_PARAMS = input message: 'Please Choise the Version of Library', ok: 'Next',
                //     //         parameters: [
                //     //             choice(name: 'VERSION_LIB', choices: getVersionsLib().join('\n'), description: 'Choise Library Versions')
                //     //         ]
                //     //     env.VERSION_LIB = INPUT_PARAMS.VERSION_LIB
                //     // }
                //     echo params.VERSION_LIB
                // }
                echo getVersionsLib()
            }
        }
        // stage("Prepare Ws") {
        //     steps {
        //         cleanWs()
        //     }
        // }
        // stage("Git clone") {
        //     steps {
        //         git 'https://github.com/ausard/Hello-World-Lamda-java.git'
        //     }
        // }
        // stage("Build application") {
        //     steps {
        //         echo env.VERSION_LIB
        //         dir("HelloWorldFunction") {
        //             sh './gradlew clean build'
        //         }
        //         script {
        //             if (params.BuildAllApp == true) {
        //                 dir("HelloWorldFunctionNew") {
        //                     sh './gradlew clean build'
        //                 }
        //             }
        //         }
        //     }
        // }
        // stage("Deploy the application") {
        //     steps {
        //         withCredentials([[$class           : 'AmazonWebServicesCredentialsBinding',
        //                           accessKeyVariable: 'AWS_ACCESS_KEY_ID',
        //                           credentialsId    : 'aws',
        //                           secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']]) {
        //             sh 'export AWS_ACCESS_KEY_ID=$AWS_ACCESS_KEY_ID'
        //             sh 'export AWS_SECRET_ACCESS_KEY=$AWS_SECRET_ACCESS_KEY'
        //             script {
        //                 if (params.BuildAllApp == true) {
        //                     sh '/usr/local/bin/sam package --template-file template_all_app.yml --output-template-file packaged.yml --s3-bucket sam-deployment-bucket-ausard'
        //                     sh '/usr/local/bin/sam deploy --template-file packaged.yml'
        //                 } else {
        //                     sh '/usr/local/bin/sam package --template-file template_p.yml --output-template-file packaged.yml --s3-bucket sam-deployment-bucket-ausard'
        //                     sh '/usr/local/bin/sam deploy --template-file packaged.yml'
        //                 }
        //             }
        //         }
        //     }
        // }
    }
}
