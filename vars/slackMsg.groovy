def call(Map m) {

    def status = m.status ?: 'SUCCESS'
    def msg = m.msg ?: ''
    def slackChannel = m.slackChannel ?: ''

    def color
    def failure
    if (status == "STARTED") {
        color = "#339c4a"
        if (msg.length() == 0) {
            msg = "started ${env.JOB_NAME} ${env.branch} (#${env.BUILD_NUMBER}) (<${env.BUILD_URL}|Open>)"
        }
        failure = false
    } else if (status == 'WARNING') {
        color = "#fcff00"
        failure = false
    } else if (status == 'ERROR') {
        color = "#d00000"
        failure = true
    } else if (status == 'UPLOADED'){
        color = "#339c4a"
        if (msg.length() == 0) {
            msg = "Apks uploaded: version X.X.X.${env.BUILD_NUMBER} (${env.branch})"
        }
        failure = false
    } else {
        color = "#339c4a"
        failure = false
    }

    if (slackChannel.length() == 0) {
        slackSend color: color, message: msg, failOnError: failure
    } else {
        slackSend color: color, message: msg, channel: slackChannel, failOnError: failure
    }
}