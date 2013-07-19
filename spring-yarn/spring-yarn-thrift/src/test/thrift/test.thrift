namespace java org.springframework.yarn.thrift.gen

typedef i32 int
typedef i64 long

service TestService {
	long add(1:int number1, 2:int number2),
}
