package com.orangebox.kit.core.exception

class BusinessException : Exception {
    constructor()
    constructor(mensagem: String?) : super(mensagem)
    constructor(mensagem: String?, t: Throwable?) : super(mensagem, t)

    companion object {
        private const val serialVersionUID = -4268005541947695051L
    }
}