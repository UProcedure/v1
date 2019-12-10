--bf.insert test capacity  10000 error 0.00001 items  a b c
local bloomName = KEYS[1]
local size = KEYS[2]
local error = KEYS[3]

local result ={}
for i = 4,#(KEYS) do
   result[i-3]= redis.call('bf.insert',bloomName,"capacity",size,"error",error,"items",KEYS[i])
end
return result