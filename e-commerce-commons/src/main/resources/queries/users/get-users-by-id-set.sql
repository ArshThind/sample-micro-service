SELECT * FROM [USERS]
WHERE status = 'A'
AND user_id IN (:userId);