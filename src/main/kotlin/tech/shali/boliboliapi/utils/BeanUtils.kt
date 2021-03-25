package tech.shali.boliboliapi.utils

import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.memberProperties

/**
 * 通过构造方法copy bean
 * 要求目标bean只有一个构造函数
 */
fun <T : Any, R : Any> T.copyPropsTo(targetClass: KClass<R>): R {
    val fromObject = this
    val mutableProps = this::class.memberProperties
    val constructors = targetClass.constructors.singleOrNull() ?: throw IllegalArgumentException("必须只有一个构造函数: $this")
    val params: MutableMap<KParameter, Any> = HashMap()
    constructors.parameters.forEach { targetProp ->
        mutableProps.find {
            it.returnType == targetProp.type && targetProp.name == it.name
        }?.let {
            val value = it.getter.call(fromObject)
            if (value == null && !targetProp.isOptional) {
                throw IllegalArgumentException("该参数不能为空: $it")
            }
            value?.let {
                params[targetProp] = value
            }
        }
    }
    return constructors.callBy(params)
}