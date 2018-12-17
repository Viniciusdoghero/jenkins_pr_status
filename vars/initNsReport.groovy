import br.com.netshoes.pipeline.NsReport

//vars/initNsReport
def call(Map m) {
    def jacoco = new NsReport()
    jacoco.logF = m.logFile
    jacoco.logF = m.logFile
    jacoco.lintFile = m.lintFile
    jacoco.pmdFile = m.pmdFile
    jacoco.checkStyleFile = m.checkStyleFile
    jacoco.findBugsFile = m.findBugsFile

    jacoco.jcTitle = m.jcTitle
    jacoco.qualityTitle = m.qualityTitle

    def teste = "echo printFromShell".execute().in.text
    echo "Print teste: ${teste}"
    return jacoco
}