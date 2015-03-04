package grails.plugin.sendmail

import grails.test.mixin.*
import org.junit.After
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
        grailsApplication.config.grails.mail.deliveryBean = "mockDeliveryService"
    }

    @After
    void tearDown() {
        grailsApplication.config.clear()
    }

    void testSend() {
        def expectedTo = "test@test.cz"
        def expectedFrom = "ja@test.cz"
        def expectedSubject = "Test Subject"
        def expectedBody = "Test Text"
        mailService.sendMail {
            to expectedTo
            from expectedFrom
            subject expectedSubject
            body expectedBody
        }

        assertEquals(expectedTo, mockDeliveryService.justSent.to[0])
        assertEquals(expectedFrom, mockDeliveryService.justSent.from)
        assertEquals(expectedSubject, mockDeliveryService.justSent.subject)
        assertEquals(expectedBody, mockDeliveryService.justSent.body)
    }

    void testDefaults() {
        def expectedFrom = "testdefault@test.cz"
        def expectedReplyTo = "noreply@test.cz"

        grailsApplication.config.grails.mail.defaultFrom = expectedFrom
        grailsApplication.config.grails.mail.defaultReplyTo = expectedReplyTo
        mailService.sendMail {
            to "testto@test.cz"
        }

        assertEquals(expectedFrom, mockDeliveryService.justSent.from)
        assertEquals(expectedReplyTo, mockDeliveryService.justSent.replyTo)

        expectedFrom = "testnotdefault@test.cz"
        mailService.sendMail {
            to "testto@test.cz"
            from expectedFrom
        }
        assertEquals(expectedFrom, mockDeliveryService.justSent.from)
    }

    void testOverrideAddress() {
        def expectedFrom = "testoverrideaddress@test.cz"
        grailsApplication.config.grails.mail.overrideAddress = expectedFrom
        mailService.sendMail {
            to "testto@test.cz"
        }

        assertEquals(expectedFrom, mockDeliveryService.justSent.to[0])
    }
}
