//vars/gitComment

def call(body) {

    // evaluate the body block, and collect configuration into the object
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    node {
        sh "curl -X POST -H 'Content-Type: application/json' " +
                "-H 'Authorization: token ${config.authToken}' ${config.commentUrl} " +
                "-d '{\"body\": \"${config.message}\"}'"
    }
}