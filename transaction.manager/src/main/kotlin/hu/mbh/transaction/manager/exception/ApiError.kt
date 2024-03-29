package hu.mbh.transaction.manager.exception

import com.fasterxml.jackson.annotation.JsonProperty
import hu.mbh.transaction.manager.model.ErrorCode
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

data class ApiError(
        @JsonProperty("status") val httpStatus: HttpStatus,
        @JsonProperty("code") val errorCode: ErrorCode,
        @JsonProperty("timestamp") val timestamp: LocalDateTime
)
