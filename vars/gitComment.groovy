//vars/gitComment

def call(Map m) {
    sh "curl -X POST -H 'Content-Type: application/json' " +
            "-H 'Authorization: token ${m.authToken}' ${m.commentUrl} " +
            "-d '{\"body\": \"${m.message}\"}'"
}