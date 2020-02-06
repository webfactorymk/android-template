package mk.webfactory.template.network.http

class ApiErrorException(
    val statusCode: Int,
    val errorFields: Map<String, String>
) : RuntimeException(String.format("Status code: %d; \nError - %s", statusCode, errorFields))