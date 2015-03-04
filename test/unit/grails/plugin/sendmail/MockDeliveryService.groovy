package grails.plugin.sendmail

/**
 * @author vdedik@redhat.com
 */
class MockDeliveryService implements DeliveryInterface {
    Mail justSent

    @Override
    def sendMail(Mail mail) {
        justSent = mail
    }
}
