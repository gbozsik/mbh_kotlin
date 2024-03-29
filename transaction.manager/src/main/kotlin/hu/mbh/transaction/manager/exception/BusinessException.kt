package hu.mbh.transaction.manager.exception

import hu.mbh.transaction.manager.model.ErrorCode
import org.springframework.http.HttpStatus

class BusinessException(message: String?, val errorCode: ErrorCode, val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST) : Exception(message) {
}