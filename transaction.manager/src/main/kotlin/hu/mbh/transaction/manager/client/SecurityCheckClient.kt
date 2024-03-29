package hu.mbh.transaction.manager.client

import hu.mbh.transaction.manager.model.SecurityCheckRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient



@Component
class SecurityCheckClient(@Value("\${client.url.security-check}") val securityCheckUrl: String) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    val restClient: RestClient = RestClient.create()

    fun callSecurityCheckClient(request: SecurityCheckRequest) {
        logger.info("Calling Security check service to validate account with account number: {} and account holder name: {}", request.accountNumber, request.accountHolderName)
        val result = restClient.post()
                .uri(securityCheckUrl)
                .contentType(APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toBodilessEntity()
        logger.info("Security check service response for account: {}, response: {}", request.accountNumber, result)
    }
}