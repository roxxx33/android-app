class CreatePosts < ActiveRecord::Migration
  def change
    create_table :posts do |t|
      t.references :user, index: true, foreign_key: true
      t.references :community, index: true, foreign_key: true
      t.text :postdata

      t.timestamps null: false
    end
  end
end
