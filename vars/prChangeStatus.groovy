//vars/prStatus

def call(body) {

    // evaluate the body block, and collect configuration into the object
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    node {

        def messageBody = "{"
        if (config.prStatus != null) {
            messageBody += "\"state\": \"${config.prStatus}\""
        }

        if (config.targetUrl != null) {
            messageBody += ", \"target_url\": \"${config.targetUrl}\""
        }

        if (config.description != null) {
            messageBody += ", \"description\": \"${config.description}\""
        }

        if (config.prContext != null) {
            messageBody += ", \"context\": \"${config.prContext}\""
        } else {
            messageBody += ", \"context\": \"CI/Jenkins\""
        }
        messageBody += "}"

        def msgBody = messageBody
        if (config.fullBody != null) {
            msgBody = config.fullBody
        }
        echo "body: "
        echo msgBody

        sh "curl -X POST -H 'Content-Type: application/json' " +
                "-H 'Authorization: token ${config.authToken}' ${config.statusesUrl} " +
                "-d '${msgBody}'"
    }
}