package grails.plugin.sendmail



import grails.test.mixin.*
import org.junit.Before

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(MailService)
class MailServiceTests {

    def mailService
    def mockDeliveryService

    @Before
    void setUp() {
        mailService = new MailService()
        mailService.grailsApplication = grailsApplication
        mockDeliveryService = new MockDeliveryService()

        grailsApplication.mainContext.registerMockBean('mockDeliveryService', mockDeliveryService)
        grailsApplication.config.grails.mail.delivery.interface = "mockDeliveryService"
    }

    void testSend() {
        def expectedTo = "test@test.cz"
        def expectedFrom = "ja@test.cz"
        def expectedSubject = "Test Subject"
        def expectedText = "Test Text"
        mailService.send {
            to expectedTo
            from expectedFrom
            subject expectedSubject
            text expectedText
        }

        assertEquals(expectedTo, mockDeliveryService.justSent.to[0])
        assertEquals(expectedFrom, mockDeliveryService.justSent.from)
        assertEquals(expectedSubject, mockDeliveryService.justSent.subject)
        assertEquals(expectedText, mockDeliveryService.justSent.text)
    }

    void testDefaults() {
        def expectedFrom = "testdefault@test.cz"
        def expectedReplyTo = "noreply@test.cz"

        grailsApplication.config.grails.mail.default.from = expectedFrom
        grailsApplication.config.grails.mail.default.replyTo = expectedReplyTo
        mailService.send {
            to "testto@test.cz"
        }

        assertEquals(expectedFrom, mockDeliveryService.justSent.from)
        assertEquals(expectedReplyTo, mockDeliveryService.justSent.replyTo)

        expectedFrom = "testnotdefault@test.cz"
        mailService.send {
            from expectedFrom
        }
        assertEquals(expectedFrom, mockDeliveryService.justSent.from)
    }

    void testOverrideAddress() {
        def expectedFrom = "testoverrideaddress@test.cz"
        grailsApplication.config.grails.mail.overrideAddress = expectedFrom
        mailService.send {
            to "testto@test.cz"
        }

        assertEquals(expectedFrom, mockDeliveryService.justSent.to[0])
    }
}
