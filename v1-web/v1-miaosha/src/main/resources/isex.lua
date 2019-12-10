--bf.insert test capacity  10000 error 0.00001 items  a b c
local bloomName = KEYS[1]

--local result ={}
--for i = 2,#(KEYS) do
--   result[i-1]= redis.call('BF.MEXISTS',bloomName,KEYS[i])
--end
--return result
return redis.call('BF.MEXISTS',bloomName,KEYS[2])