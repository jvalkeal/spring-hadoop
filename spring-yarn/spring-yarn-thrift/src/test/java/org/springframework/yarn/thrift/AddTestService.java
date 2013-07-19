package org.springframework.yarn.thrift;

import org.apache.thrift.TException;
import org.springframework.yarn.thrift.gen.TestService;

public class AddTestService implements TestService.Iface {

	@Override
	public long add(int number1, int number2) throws TException {
		return number1+number2;
	}

}
