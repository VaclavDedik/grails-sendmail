package grails.plugin.sendmail

class MailService {

    def grailsApplication

    Mail mail

    def send(Closure closure) {
        mail = new Mail()
        mail.from = grailsApplication.config.grails.mail.default.from
        mail.replyTo = grailsApplication.config.grails.mail.default.replyTo

        closure.delegate = this
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.call()

        def serviceName = grailsApplication.config.grails.mail.delivery.interface
        DeliveryInterface service = grailsApplication.mainContext.getBean(serviceName)

        def overrideAddress = grailsApplication.config.grails.mail.overrideAddress
        if (overrideAddress) {
            to(overrideAddress)
        }

        service.sendMail(mail)
    }

    def to(String... to) {
        mail.to = to
    }

    def from(String from) {
        mail.from = from
    }

    def replyTo(String replyTo) {
        mail.replyTo = replyTo
    }

    def cc(String... cc) {
        mail.cc = cc
    }

    def bcc(String... bcc) {
        mail.bcc = bcc
    }

    def subject(String subject) {
        mail.subject = subject
    }

    def text(String text) {
        mail.text = text
    }

    def text(String view, Map model) {
        // Not implemented yet
    }
}
