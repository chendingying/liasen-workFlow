package com.liansen.flow.page;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by CDZ on 2018/11/1.
 */
@Service
public class PageService {
    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * 只查询一列数据类型对象。用于只有一行查询结果的数据
     * @param sql
     * @param params
     * @param cla Integer.class,Float.class,Double.Class,Long.class,Boolean.class,Char.class,Byte.class,Short.class
     * @return
     */
    public Object queryOneColumnForSigetonRow(String sql,Object[] params,Class cla){
        Object result=null;
        try{
            if(params==null||params.length>0){
                result=jdbcTemplate.queryForObject(sql,params,cla);
            }else{
                result=jdbcTemplate.queryForObject(sql,cla);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * 查询返回实体对象集合
     * @param sql    sql语句
     * @param params 填充sql问号占位符数
     * @param cla    实体对象类型
     * @return
     */
    public List queryForObjectList(String sql, Object[] params, final Class cla){
        final List list=new ArrayList();
        try{

            jdbcTemplate.query(sql, params, new RowCallbackHandler(){
                public void processRow(ResultSet rs) {
                    try{
                        List<String> columnNames=new ArrayList<String>();
                        ResultSetMetaData meta=rs.getMetaData();
                        int num=meta.getColumnCount();
                        for(int i=0;i<num;i++){
                            columnNames.add(meta.getColumnLabel(i+1).toLowerCase().trim());
                        }
                        Method[] methods=cla.getMethods();
                        List<String> fields=new ArrayList<String>();
                        for(int i=0;i<methods.length;i++){
                            if(methods[i].getName().trim().startsWith("set")){
                                String f=methods[i].getName().trim().substring(3);
                                f=(f.charAt(0)+"").toLowerCase().trim()+f.substring(1);
                                fields.add(f);
                            }
                        }
                        do{
                            Object obj=null;
                            try{
                                obj=cla.getConstructor().newInstance();
                            }catch(Exception ex){
                                ex.printStackTrace();
                            }
                            for(int i=0;i<num;i++){
                                Object objval=rs.getObject(i+1);
                                for(int n=0;n<fields.size();n++){
                                    String fieldName=fields.get(n).trim();
                                    if(columnNames.get(i).equals(fieldName.toLowerCase().trim())){
                                        BeanUtils.copyProperty(obj, fieldName, objval);
                                        break;
                                    }
                                }
                            }
                            list.add(obj);
                        }while(rs.next());
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                }
            });
        }catch(Exception ex){ex.printStackTrace();}
        if(list.size()<=0){
            return null;
        }
        return list;
    }

    /**
     * 查询返回List<Map<String,Object>>格式数据,每一个Map代表一行数据，列名为key
     * @param sql  sql语句
     * @param params 填充问号占位符数
     * @return
     */
    public List<Map<String,Object>> queryForMaps(String sql, Object[] params){
        try{
            if(params!=null&&params.length>0){
                return jdbcTemplate.queryForList(sql, params);
            }
            return jdbcTemplate.queryForList(sql);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 查询分页（MySQL数据库）
     * @param sql     终执行查询的语句
     * @param params  填充sql语句中的问号占位符数
     * @param page    想要第几页的数据
     * @param pagerow 每页显示多少条数
     * @param cla     要封装成的实体元类型
     * @return        pageList对象
     */
    public PageList queryByPageForMySQL(String sql, Object[] params, int page, int pagerow,Class cla){
        String rowsql="select count(*) from ("+sql+") gmtxtabs_";   //查询总行数sql
        int pages = 0;   //总页数
        int rows=(Integer)queryOneColumnForSigetonRow(rowsql, params, Integer.class);  //查询总行数
        //判断页数,如果是页大小的整数倍就为rows/pageRow如果不是整数倍就为rows/pageRow+1
        if (rows % pagerow == 0) {
            pages = rows / pagerow;
        } else {
            pages = rows / pagerow + 1;
        }
        //查询第page页的数据sql语句
        if(page<=1){
            sql+=" limit 0,"+pagerow;
        }else{
            sql+=" limit "+((page-1)*pagerow)+","+pagerow;
        }
        //查询第page页数据
        List list=null;
        if(cla!=null){
            list=queryForObjectList(sql, params, cla);
        }else{
            list=queryForMaps(sql, params);
        }

        //返回分页格式数据
        PageList pl =new PageList();
        pl.setPage(page);  //设置显示的当前页数
        pl.setPages(pages);  //设置总页数
        pl.setData(list);   //设置当前页数据
        pl.setTotal(rows);    //设置总记录数
        return pl;
    }
}
