using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class CameraController : MonoBehaviour {

	public GameObject player; //reference to the player
	
	private Vector3 offset; 
	

	// Use this for initialization
	void Start () {
		offset = transform.position - player.transform.position; 
	}
	
	// Update is called once per frame
	//LateUpdate runs after all items are processed in update
	void LateUpdate () {
		transform.position = player.transform.position + offset; 
		
	}
}
