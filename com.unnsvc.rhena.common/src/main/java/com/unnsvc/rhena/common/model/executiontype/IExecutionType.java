
package com.unnsvc.rhena.common.model.executiontype;

public interface IExecutionType {

	public static final IModelExecutionType MODEL = new IModelExecutionType() {

		@Override
		public String literal() {

			return "model";
		}
	};

	public static final IFrameworkExecutionType FRAMEWORK = new IFrameworkExecutionType() {

		@Override
		public String literal() {

			return "framework";
		}
	};

	public static final IDeliverableExecutionType DELIVERABLE = new IDeliverableExecutionType() {

		@Override
		public String literal() {

			return "deliverable";
		}
	};

	public static final ITestExecutionType TEST = new ITestExecutionType() {

		@Override
		public String literal() {

			return "test";
		}
	};

	public static final IIntegrationExecutionType INTEGRATION = new IIntegrationExecutionType() {

		@Override
		public String literal() {

			return "integration";
		}
	};

	public static final IPrototypeExecutionType PROTOTYPE = new IPrototypeExecutionType() {

		@Override
		public String literal() {

			return "prototype";
		}
	};
	
	public static final ILifecycleExecutionType LIFECYCLE = new ILifecycleExecutionType() {
		
		@Override
		public String literal() {
			
			return "lifecycle";
		}
	};

	public String literal();

}
