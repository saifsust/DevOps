### SimpleAliasRegistry
- Map of aliasToBeanName
- Keep AliasToBeanName (called register)
- Keep AliasToBeanName and detect circular Alias (called resolve)
- detect circular Alias
- remove alias
- get all alias
- get primary (canonical) bean name using a bean/alias name

#### SimpleBeanDefinitionRegistry (BeanDefinitionRegistry)
- Map of beanNameToBeanDefinition
- keep BeanDefinition (called register)
- remove BeanDefinition
- get BeanDefinition
- contain BeanDefinition
- get all BeanDefinition names
- check if beanName is in use ( isAlias or beanDefinition exist)

#### DefaultSingletonBeanRegistry
- Map of beanNameToSingletonObject
- add SingletonObject to beanNameToSingletonObject (called register)
- - remove SingletonObject from beanNameToSingletonObject
- - remove earlySingletonObject from beanNameToEarlySingletonObjects
- - add registrationSingletons. 