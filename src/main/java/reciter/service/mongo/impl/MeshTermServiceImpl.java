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
package reciter.service.mongo.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reciter.database.mongo.model.MeshTerm;
import reciter.database.mongo.repository.MeshTermRepository;
import reciter.service.mongo.MeshTermService;

@Service("meshTermService")
public class MeshTermServiceImpl implements MeshTermService {

	@Autowired
	private MeshTermRepository meshTermRepository;
	
	@Override
	public void save(List<MeshTerm> meshTerms) {
		meshTermRepository.save(meshTerms);
	}
	
	@Override
	public List<MeshTerm> findAll() {
		return meshTermRepository.findAll();
	}
}
