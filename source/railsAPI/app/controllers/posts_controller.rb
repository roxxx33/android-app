class PostsController < ApplicationController
	before_action :authenticate_user!
	respond_to :json
	def create
		@post = Post.create(params.permit(:postdata).merge(community_id: current_user.community_id, user_id: current_user.id))
		if @post.save
			render :json => @post
		end
	end
end