-- reward_allocation.lua

-- KEYS[1] = participants ZSET key
-- KEYS[2] = rewards HASH key

-- ARGV[1] = userId
-- ARGV[2] = currentTimestamp
-- ARGV[3..] = reward plan (amount1, count1, amount2, count2, ...)

local userId = ARGV[1]
local timestamp = ARGV[2]

if redis.call("HEXISTS", KEYS[2], userId) == 1 then
    return -1
end

redis.call("ZADD", KEYS[1], timestamp, userId)

local rank = redis.call("ZRANK", KEYS[1], userId)
rank = rank + 1

local i = 3
local cumulative = 0
while i <= #ARGV do
    local amount = tonumber(ARGV[i])
    local count = tonumber(ARGV[i + 1])
    cumulative = cumulative + count
    if rank <= cumulative then
        redis.call("HSET", KEYS[2], userId, amount)
        return amount
    end
    i = i + 2
end

return 0
