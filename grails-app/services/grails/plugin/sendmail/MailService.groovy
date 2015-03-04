package grails.plugin.sendmail

class MailService {

    def grailsApplication

    Mail mail

    def send(Closure closure) {
        mail = new Mail()
        def defaultFrom = grailsApplication.config.grails.mail.defaultFrom
        def defaultReplyTo = grailsApplication.config.grails.mail.defaultReplyTo
        if (defaultFrom) {
            from(defaultFrom)
        }
        if (defaultReplyTo) {
            replyTo(defaultReplyTo)
        }

        closure.delegate = this
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.call()

        if (!mail.to) {
            throw new IllegalArgumentException("Mail field 'to' is required.")
        }

        String serviceName = grailsApplication.config.grails.mail.deliveryBean
        DeliveryInterface service = (DeliveryInterface) grailsApplication.mainContext.getBean(serviceName)

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
        throw new UnsupportedOperationException()
    }
}
