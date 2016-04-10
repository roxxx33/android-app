class CommunitiesController < ApplicationController
	before_action :authenticate_user!
	def show
		@posts = Post.where(:community_id => params[:id])
		render json: custom_json_for(@posts)
	end

	def create
		@community = Community.create(params.permit(:name, :location, :description).merge(user_id: current_user.id))
		@user = User.find_by_id(current_user.id)
		@user.community_id = @community.id
		@user.save
		respond_with @community
	end
	private
	def custom_json_for(value)
		list = value.map do |client|
			{
				:id => client.id,
				:name => User.find(client.user_id).name,
				:community_id => client.community_id,
				:postdata => client.postdata,
				:created_at => client.created_at
			}
		end
		list.to_json
	end
end