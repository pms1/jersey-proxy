package jerseyproxy;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

class Model {
	  Map<Method, ModelMethod> methods;

	Model(Map < Method, ModelMethod > methods) {
		 this.methods = methods;
	 }
}
