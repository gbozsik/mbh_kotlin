package hu.mbh.transaction.manager.exception

import hu.mbh.transaction.manager.model.ErrorCode

class BusinessException(message: String?, val errorCode: ErrorCode) : Exception(message) {
}