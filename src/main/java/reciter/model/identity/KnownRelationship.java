/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package reciter.model.identity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel.DynamoDBAttributeType;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

@DynamoDBDocument
public class KnownRelationship {

	private String uid;
	private AuthorName name;
	@DynamoDBTyped(DynamoDBAttributeType.S)
	private RelationshipType type;
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public AuthorName getName() {
		return name;
	}
	public void setName(AuthorName name) {
		this.name = name;
	}
	public RelationshipType getType() {
		return type;
	}
	public void setType(RelationshipType type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "KnownRelationship [uid=" + uid + ", name=" + name + ", type=" + type + "]";
	}
	
	public enum RelationshipType {
		
		CO_INVESTIGATOR,
		MENTOR,
		MENTEE,
		MANAGER,
		REPORT,
		HR
		
	}
}
