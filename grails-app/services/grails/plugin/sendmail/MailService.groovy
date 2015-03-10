package grails.plugin.sendmail

class MailService {

    def grailsApplication
    def groovyPageRenderer

    Mail mail

    def sendMail(Closure closure) {
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

    def to(... to) {
        mail.to = to.collect([], {it.toString()})
    }

    def from(from) {
        mail.from = from
    }

    def replyTo(replyTo) {
        mail.replyTo = replyTo
    }

    def cc(... cc) {
        mail.cc = cc.collect([], {it.toString()})
    }

    def bcc(... bcc) {
        mail.bcc = bcc.collect([], {it.toString()})
    }

    def subject(subject) {
        mail.subject = subject
    }

    def body(body) {
        mail.body = body
    }

    def body(String view, Map model) {
        mail.body = groovyPageRenderer.render(view: view, model: model)
        mail.type = Mail.Type.HTML
    }

    def text(text) {
        body(text)
    }

    def html(html) {
        body(html)
        mail.type = Mail.Type.HTML
    }

    def html(String view, Map model) {
        body(view, model)
    }
}
