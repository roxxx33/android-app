class RegistrationsController < DeviseTokenAuth::RegistrationsController
	skip_before_filter :verify_authenticity_token
  	def create
      super
  	end
  	def update
  		super
  	end
  	
  	def destroy
  		super
  	end
  	
  	def edit
  		super
  	end
  	respond_to :json
end