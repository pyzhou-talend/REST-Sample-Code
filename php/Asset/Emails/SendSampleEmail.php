<?php
/*
   SendSampleEmail.php

   Marketo REST API Sample Code
   Copyright (C) 2016 Marketo, Inc.

   This software may be modified and distributed under the terms
   of the MIT license.  See the LICENSE file for details.
*/
$sample = new SendSampleEmail();
$sample->id = 1;
$sample->emailAddress = "john.doe@marketo.com";
print_r($sample->postData());

class SendSampleEmail{
	private $host = "CHANGE ME";
	private $clientId = "CHANGE ME";
	private $clientSecret = "CHANGE ME";
	public $id; //id of  to delete
	public $emailAddress;//email address to send to
	public $textOnly;//boolean option to send text only version
	public $leadId;// id of lead to impersonate
	
	public function postData(){
		$url = $this->host . "/rest/asset/v1/email/" . $this->id . "/sendSample.json?access_token=" . $this->getToken();
		$requestBody  = "&emailAddress=" . $this->emailAddress;
		if (isset($this->textOnly)){
			$requestBody .= "&textOnly=" . $this->textOnly;
		}
		if (isset($this->leadId)){
			$requestBody .= "&leadId=" . $this->leadId;
		}
		$ch = curl_init($url);
		curl_setopt($ch,  CURLOPT_RETURNTRANSFER, 1);
		curl_setopt($ch, CURLOPT_HTTPHEADER, array('accept: application/json'));
		curl_setopt($ch, CURLOPT_POST, 1);
		curl_setopt($ch, CURLOPT_POSTFIELDS, $requestBody);
		curl_getinfo($ch);
		$response = curl_exec($ch);
		return $response;
	}
	
	private function getToken(){
		$ch = curl_init($this->host . "/identity/oauth/token?grant_type=client_credentials&client_id=" . $this->clientId . "&client_secret=" . $this->clientSecret);
		curl_setopt($ch,  CURLOPT_RETURNTRANSFER, 1);
		curl_setopt($ch, CURLOPT_HTTPHEADER, array('accept: application/json',));
		$response = json_decode(curl_exec($ch));
		curl_close($ch);
		$token = $response->access_token;
		return $token;
	}	
}
