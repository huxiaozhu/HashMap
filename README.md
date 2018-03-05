# HashMap
手写实现HashMap（传参和不传参，Put以及get）

HashMap实现的原理：

1.HashMap会根据Key计算出对应的Hash码

2.hash码与数组的长度进行与运算得出的值就是在数组中的实际位置

3.HashMap是数组与单链表的结合，即2之后若在数组的值相同，就会添加在数组对应位置的链表上
