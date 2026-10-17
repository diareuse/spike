package spike.lifecycle.viewmodel

import androidx.lifecycle.ViewModel
import spike.Key
import kotlin.reflect.KClass

@Key
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.BINARY)
public annotation class ViewModelKey(val value: KClass<out ViewModel>)
