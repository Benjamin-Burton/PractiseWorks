/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package burt.music.practiseworks.data

import kotlinx.coroutines.flow.Flow

class OfflineStudentsRepository(private val studentDao: StudentDao) : StudentsRepository {
    override fun getAllStudentsStream(): Flow<List<Student>> = studentDao.getAllStudents()

    override fun getStudentStream(id: Int): Flow<Student?> = studentDao.getStudent(id)

    override suspend fun insertStudent(student: Student) = studentDao.insert(student)

    override suspend fun deleteStudent(student: Student) = studentDao.delete(student)

    override suspend fun updateStudent(student: Student) = studentDao.update(student)
}
