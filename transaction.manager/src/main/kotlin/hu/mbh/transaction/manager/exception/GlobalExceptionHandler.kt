package hu.mbh.transaction.manager.exception

import hu.mbh.transaction.manager.model.ErrorCode
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.LocalDateTime

@ControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [BusinessException::class])
    protected fun handleBusinessException(businessException: BusinessException): ResponseEntity<ApiError> =
            ResponseEntity(ApiError(HttpStatus.BAD_REQUEST, businessException.errorCode, LocalDateTime.now()), HttpStatus.BAD_REQUEST)

    @ExceptionHandler(value = [IllegalArgumentException::class])
    protected fun handleIllegalArgumentException(illegalArgumentException: IllegalArgumentException): ResponseEntity<ApiError> {
        logger.error(illegalArgumentException.message)
        return ResponseEntity(ApiError(HttpStatus.BAD_REQUEST, ErrorCode.TRANSACTION_007, LocalDateTime.now()), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [Exception::class])
    protected fun handleException(exception: BusinessException): ResponseEntity<ApiError> =
            ResponseEntity(ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.TRANSACTION_007, LocalDateTime.now()), HttpStatus.INTERNAL_SERVER_ERROR)
}