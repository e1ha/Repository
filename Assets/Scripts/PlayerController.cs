﻿using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI; 

public class PlayerController : MonoBehaviour {

	public float speed; 
	public Text countText; 
	public Text winText; 
	
	private Rigidbody rb; //creates a variable to hold the Rigidbody functions
	private int count; 
	
	void Start () 
	{
		rb = GetComponent<Rigidbody>(); 
		count = 0; 
		SetCountText(); 
		winText.text = ""; //empty string
	}
	
	void FixedUpdate () 
	{
		//record input from the horizontal and vertical axis
		float moveHorizontal = Input.GetAxis("Horizontal"); 
		float moveVertical = Input.GetAxis("Vertical"); 
		
		Vector3 movement = new Vector3(moveHorizontal,0.0f,moveVertical); 
		
		rb.AddForce(movement*speed); 
	}
	
	void OnTriggerEnter(Collider other)
	{
		if (other.gameObject.CompareTag("Pick Up")) 
		{
			other.gameObject.SetActive(false); 
			count = count + 1; 
			SetCountText(); 
			
		}
	}
	
	void SetCountText() 
	{
		countText.text = "Count: " + count.ToString(); 
		if (count >= 12) 
		{
			winText.text = "You Win!"; 
		}
	}
}
