Given user 'A' logged in with Admin role

- User 'A' should be able filter list of students by their group
- User 'A' should be able filter list of schedules by interval of dates
- User 'A' should be able reset all filters (schedules and students)

Given User 'B' logged in with Staff role

- User 'B' should be able filter list of students by their group
- User 'B' should be able filter list of schedules by interval of dates
- User 'B' should be able reset all filters (schedules and students)

Given User 'C' logged in with Teacher or Student role

- User 'C' should be able filter list of students by their group
- User 'C' should be able filter list of schedules by interval of dates
- User 'C' should be able reset all filters (schedules and students)

Given User 'D' is not logged in

- User 'D' should be able list all courses (without details)
- User 'D' should be able list all teachers (without details)