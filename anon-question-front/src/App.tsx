import { useState, useEffect } from 'react'
import bridge from "@vkontakte/vk-bridge";
import {
  AppRoot, SplitLayout, SplitCol, Panel, PanelHeader,
  Group, Card, Div, Button, Placeholder, Spinner, ConfigProvider,
} from '@vkontakte/vkui'
import '@vkontakte/vkui/dist/vkui.css'


function App() {
  const [user, setUser]=useState<{id: number; first_name: string} | null>(null)
  const [error, setError]=useState<string | null>(null)

  useEffect(()=>{
  bridge.send('VKWebAppInit').catch(console.error)

  bridge.send('VKWebAppGetUserInfo' )
  .then((data: any)=>{
    setUser({id: data.id, first_name: data.first_name})
  })
.catch((err)=>{
  console.log('VK Bridge error:', err)

  const isLocal=window.location.hostname==='localhost'
  const isNelife=window.location.hostname.includes('netlify.app')


  if(isLocal || isNelife){
    setUser({id: 123456, first_name: 'Тест'})
  }else{
  setError('Пожалуйста, откройте приложение внутри ВКонтакте')
  }
})

},[])

  return (
    <ConfigProvider>
      <AppRoot>
        <SplitLayout>
          <SplitCol>
           <Panel id="main">
           <PanelHeader>Анонимные вопросы</PanelHeader>
           {error && (
            <Div><Placeholder>{error} </Placeholder></Div>
           )}

           {!user && !error ? (
            <Div><Spinner size="l"/></Div>
           ) : user ? (
            <Group>
              <Placeholder>
                Привет, ${user.first_name}!<br/>
                Это твой тестовый экран. VK ID: {user.id}
              </Placeholder>
              <Card>
                <Div>
                  <Button size='l' stretched mode="primary" onClick={()=> alert('Кнопка работает!')}>
                    Скоро здесь будет твоя ссылка
                  </Button>
                </Div>
              </Card>
            </Group>
           ) : null}
           </Panel>
           </SplitCol>
           </SplitLayout>
           </AppRoot>
           </ConfigProvider>



  )
}

export default App
