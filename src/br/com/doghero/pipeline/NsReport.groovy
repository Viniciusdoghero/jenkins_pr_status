package br.com.doghero.pipeline

class NsReport {

    def logF
    def lintFile
    def pmdFile
    def checkStyleFile
    def findBugsFile

    def jcTitle
    def qualityTitle

    def lintIssue
    def pmdIssue
    def checkStyleIssue
    def findBugsIssue

    def jcClass
    def jcBranch
    def jcMethod
    def jcLine
    def jcInstruction
    def jacocoBranch = ""
    def jacocoReportStr
    def jacocoGitStr
    def jacocoSuccess = false

    def parseJacoco() {
        def jacocoBranch = ""
        jacocoReportStr = jcTitle + "\n"
        jacocoGitStr = ">" + jcTitle + "<br>"
        jacocoSuccess = false

        jacocoReport = sh(
                script: "egrep \'Overall coverage: class: (.*?), method: (.*?), line: (.*?), branch: (.*?), instruction: (.*?)\' ${logF}",
                returnStdout: true
        ).trim()
        jcBranch = sh(
                script: "echo '${jacocoReport}' | perl -nle '/Overall coverage: class: (.*?), method: (.*?), line: (.*?), branch: (.*?), instruction: (.*?)/; print \"\$4\"'",
                returnStdout: true
        ).trim() as int
        def scriptTest = "print \"Class: \$1\nClass:\$2\nLine: \$3\n*Branch: \$4%*\""
        def scriptJacocoGit = "print \"Class: \$1<br>Class:\$2<br>Line: \$3<br>**Branch: \$4%**\""
        jacocoReportStr += sh(
                script: "echo '${jacocoReport}' | perl -nle '/Overall coverage: class: (.*?), method: (.*?), line: (.*?), branch: (.*?), instruction: (.*?)/; ${scriptTest}'",
                returnStdout: true
        ).trim()

        jacocoGitStr += sh(
                script: "echo '${jacocoReport}' | perl -nle '/Overall coverage: class: (.*?), method: (.*?), line: (.*?), branch: (.*?), instruction: (.*?)/; ${scriptJacocoGit}'",
                returnStdout: true
        ).trim()

        echo "+++Jacoco Branch[class] is: ${jcBranch}"
//            if (jcBranch < 70) {
//                jacocoSuccess = false
//                sendSlackFailure(jacocoReportStr)
//            } else {
//                echo "+++Setting build success to upload to Beta"
//                jacocoSuccess = true
//                slackSend color: "#339c4a", message: jacocoReportStr
//            }

//            if (jacocoGitStr.length() > 0) commentGithub(jacocoGitStr)
    }

    def parseLint() {
        lintIssue = sh(
                script: "egrep \'</issue>\' ${lintFile} | wc -l",
                returnStdout: true
        ).trim()
    }

    def parsePmd() {
        pmdIssue = sh(
                script: "egrep \'</violation>\' ${pmdFile} | wc -l",
                returnStdout: true
        ).trim()
    }

    def parseCheckStyle() {
        checkStyleIssue = sh(
                script: "egrep \'<error (.*?)/>\' ${checkStyleFile} | wc -l",
                returnStdout: true
        ).trim()
    }

    def parseFindBugs() {
        findBugsIssue = sh(
                script: "egrep \'<FindBugsSummary timestamp=(.*?) total_classes=(.*?) referenced_classes=(.*?) total_bugs=(.*?)\' ${findBugsFile} | awk \'{print \$10}\'",
                returnStdout: true
        ).trim()
    }

    def getQualityReport() {
        def warningMessage = qualityTitle

        if (checkStyleIssue != null && checkStyleIssue.length() > 0) {
            warningMessage += "\nCheckstyle issues: ${checkStyleIssue}"
        }
        if (lintIssue != null && lintIssue.length() > 0) {
            warningMessage += "\nLint issues: ${lintIssue}"
        }
        if (pmdIssue != null && pmdIssue.length() > 0) {
            warningMessage += "\nPMD issues: ${pmdIssue}"
        }

        if (findBugsIssue != null && findBugsIssue.length() > 0) {
            findBugsIssue = findBugsIssue.replaceAll("total_bugs=\"", '')
            findBugsIssue = findBugsIssue.replaceAll("\"", '')

            warningMessage += "\nFindBugs issues: ${findBugsIssue}"
        }

        return [slackMessage: warningMessage, gitMessage: ">${warningMessage.replace("\\n", "<br>")}"]
    }

    def getJcReport() {
        return [slackMessage: jacocoReportStr, gitMessage: ">${jacocoReportStr.replace("\\n", "<br>")}"]
    }

}
