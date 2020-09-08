// def getLibVersions() {
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
    def versions = new ArrayList()
    for (version in metadata.versioning.versions) {
        versions.add(version.text())
    }
    println versions.join('\n')
    return metadata.versioning.versions.version*.text()
// }
