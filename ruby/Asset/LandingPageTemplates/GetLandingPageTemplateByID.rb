#
# GetLandingPageTemplateByID.rb
#
# Marketo REST API Sample Code
# Copyright (C) 2016 Marketo, Inc.
#
# This software may be modified and distributed under the terms
# of the MIT license.  See the LICENSE file for details.
#
require 'rest-client'
require 'json'

host = "CHANGE ME"
client_id = "CHANGE ME"
client_secret = "CHANGE ME"

def get_token(host, client_id, client_secret)
  url = "#{host}/identity/oauth/token?grant_type=client_credentials&client_id=#{client_id}&client_secret=#{client_secret}"
  response = RestClient.get url
  json = JSON.parse(response)
  return json["access_token"]
end


id = 1002 #id of template to retrieve
params = {
  :access_token => get_token(host, client_id, client_secret),
  :status => "Approved" #status filter, Draft or Approved
}

response = RestClient.get "#{host}/rest/asset/v1/landingPageTemplate/#{id}.json", {:params => params}

puts response