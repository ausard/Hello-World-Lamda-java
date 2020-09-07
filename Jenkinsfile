#!groovy
def getLibVersions() {
    // final API_KEY = "FOOBARAPIKEY"
    // final REPO_NAME = "service-docker"
    // final APP_NAME = "myapp"

    // def cmd = [ 'bash', '-c', "curl -H 'X-JFrog-Art-Api: ${API_KEY}' https://artifactory.acme.co/artifactory/api/docker/${REPO_NAME}/v2/${APP_NAME}/tags/list".toString()]
    // def result = cmd.execute().text

    // def slurper = new JsonSlurper()
    // def json = slurper.parseText(result)
    // def tags = new ArrayList()
    // if (json.tags == null || json.tags.size == 0)
    //   tags.add("unable to fetch tags for ${APP_NAME}")
    // else
    //   tags.addAll(json.tags)
    def metadata = new XmlSlurper().parse("http://18.159.141.245:8081/nexus/content/repositories/releases/hw/libs/common/helloworldlib/maven-metadata.xml")
    // println metadata.versioning.latest
    // println metadata.versioning.versions.version*.text()
    return metadata
}
pipeline {
    agent {
        label("agent")
    }
    options {
        timestamps()
    }
    parameters {
        booleanParam defaultValue: false, description: 'Building All Apps', name: 'BuildAllApp'
    }
    stages {        
        stage("Gather Deployment Parameters") {
            steps {
                timeout(time: 30, unit: 'SECONDS') {
                    script {
                        def versions = getLibVersions()
                        echo versions.versioning.versions.version*.text()
                    //     // Show the select input modal
                    //    def INPUT_PARAMS = input message: 'Please Provide Parameters', ok: 'Next',
                    //                     parameters: [
                    //                     choice(name: 'ENVIRONMENT', choices: ['dev','qa'].join('\n'), description: 'Please select the Environment'),
                    //                     choice(name: 'IMAGE_TAG', choices: getDockerImages(), description: 'Available Docker Images')]
                    //     env.ENVIRONMENT = INPUT_PARAMS.ENVIRONMENT
                    //     env.IMAGE_TAG = INPUT_PARAMS.IMAGE_TAG
                    }
                }
            }
        }
        stage("Prepare Ws") {
            steps {
                cleanWs()
            }
        }
        stage("Git clone") {
            steps {
                git 'https://github.com/ausard/Hello-World-Lamda-java.git'
            }
        }        
        stage("Build application") {
            steps {
                dir("HelloWorldFunction") {
                    sh './gradlew clean build'
                }
                script {
                    if (params.BuildAllApp == true) {
                        dir("HelloWorldFunctionNew") {
                            sh './gradlew clean build'
                        }
                    }
                }
            }
        }
        stage("Deploy the application") {
            steps {
                withCredentials([[$class           : 'AmazonWebServicesCredentialsBinding',
                                  accessKeyVariable: 'AWS_ACCESS_KEY_ID',
                                  credentialsId    : 'aws',
                                  secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']]) {
                    sh 'export AWS_ACCESS_KEY_ID=$AWS_ACCESS_KEY_ID'
                    sh 'export AWS_SECRET_ACCESS_KEY=$AWS_SECRET_ACCESS_KEY'
                    script {
                        if (params.BuildAllApp == true) {
                            sh '/usr/local/bin/sam package --template-file template_all_app.yml --output-template-file packaged.yml --s3-bucket sam-deployment-bucket-ausard'
                            sh '/usr/local/bin/sam deploy --template-file packaged.yml'
                        } else {
                            sh '/usr/local/bin/sam package --template-file template_p.yml --output-template-file packaged.yml --s3-bucket sam-deployment-bucket-ausard'
                            sh '/usr/local/bin/sam deploy --template-file packaged.yml'
                        }
                    }
                }
            }
        }
    }
}
