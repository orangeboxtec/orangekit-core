package com.orangebox.kit.core.utils

import org.reflections.Reflections
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
class TypeServiceImpl : TypeService {
    override fun listTypes(): List<String?>? {
        val emf: EntityManagerFactory = Persistence.createEntityManagerFactory("startupkit")
        val mm: Metamodel = emf.getMetamodel()
        val list: MutableList<String?> = mm.getManagedTypes().stream()
            .map(ManagedType::getJavaType)
            .map(Class::getTypeName).collect(java.util.stream.Collectors.toList())
        val ref = Reflections("com.mangobits.startupkit")
        list.addAll(
            ref.getTypesAnnotatedWith(Embeddable::class.java).stream()
                .map(Class::getTypeName).collect(java.util.stream.Collectors.toList())
        )
        return list
    }
}