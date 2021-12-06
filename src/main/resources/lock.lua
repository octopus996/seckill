#ARGV随机数组
if redis.call("get",kEYS[1])==ARGV[1] then
    return redis.call("del",HEYS[1])
else
    return 0
end