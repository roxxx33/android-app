Rails.application.config.middleware.use OmniAuth::Builder do
  provider :facebook, "1168270519852955", "52b96e57458d832ac72fb76e8edeadae"
end