//vars/prStatus
def call(Map m) {

    def messageBody = "{"
    if (m.prStatus != null) {
        messageBody += "\"state\": \"${m.prStatus}\""
    }

    if (m.targetUrl != null) {
        messageBody += ", \"target_url\": \"${m.targetUrl}\""
    }

    if (m.description != null) {
        messageBody += ", \"description\": \"${m.description}\""
    }

    if (m.prContext != null) {
        messageBody += ", \"context\": \"${m.prContext}\""
    } else {
        messageBody += ", \"context\": \"CI/Jenkins\""
    }
    messageBody += "}"

    def msgBody = messageBody
    if (m.fullBody != null) {
        msgBody = config.fullBody
    }
    echo "body: "
    echo msgBody

    sh "curl -X POST -H 'Content-Type: application/json' " +
            "-H 'Authorization: token ${m.authToken}' ${m.statusesUrl} " +
            "-d '${msgBody}'"

}